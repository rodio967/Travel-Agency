
-- 1) Виза не выдаётся детям (CHILD).

CREATE OR REPLACE FUNCTION trg_visa_not_for_child_fn()
    RETURNS TRIGGER AS $$
DECLARE
    v_type VARCHAR;
BEGIN
    SELECT tourist_type INTO v_type FROM tourist WHERE id = NEW.tourist_id;
    IF v_type = 'CHILD' THEN
        RAISE EXCEPTION 'Cannot issue visa to a CHILD tourist (tourist_id=%)', NEW.tourist_id
            USING ERRCODE = 'check_violation';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;;

DROP TRIGGER IF EXISTS trg_visa_not_for_child ON visa;;

CREATE TRIGGER trg_visa_not_for_child
    BEFORE INSERT OR UPDATE ON visa
    FOR EACH ROW EXECUTE FUNCTION trg_visa_not_for_child_fn();;


-- 2) Груз оформляется только на shop-туристов.

CREATE OR REPLACE FUNCTION trg_cargo_only_for_shop_fn()
    RETURNS TRIGGER AS $$
DECLARE
    v_type VARCHAR;
BEGIN
    SELECT tourist_type INTO v_type FROM tourist WHERE id = NEW.tourist_id;
    IF v_type IS DISTINCT FROM 'SHOP' THEN
        RAISE EXCEPTION 'Cargo can only be registered for SHOP tourists, got % (tourist_id=%)',
            v_type, NEW.tourist_id
            USING ERRCODE = 'check_violation';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;;

DROP TRIGGER IF EXISTS trg_cargo_only_for_shop ON cargo;;

CREATE TRIGGER trg_cargo_only_for_shop
    BEFORE INSERT OR UPDATE ON cargo
    FOR EACH ROW EXECUTE FUNCTION trg_cargo_only_for_shop_fn();;


-- 3) На экскурсии записываются только REST

CREATE OR REPLACE FUNCTION trg_excursion_not_for_shop_fn()
    RETURNS TRIGGER AS $$
DECLARE
    v_type VARCHAR;
BEGIN
    SELECT tourist_type INTO v_type FROM tourist WHERE id = NEW.tourist_id;
    IF v_type = 'SHOP' THEN
        RAISE EXCEPTION 'SHOP tourists do not go on excursions (tourist_id=%)', NEW.tourist_id
            USING ERRCODE = 'check_violation';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;;

DROP TRIGGER IF EXISTS trg_excursion_not_for_shop ON excursion_booking;;

CREATE TRIGGER trg_excursion_not_for_shop
    BEFORE INSERT OR UPDATE ON excursion_booking
    FOR EACH ROW EXECUTE FUNCTION trg_excursion_not_for_shop_fn();;

-- 4) Ребёнок и его родитель — в одной группе; родитель не CHILD.

CREATE OR REPLACE FUNCTION trg_child_parent_consistency_fn()
    RETURNS TRIGGER AS $$
DECLARE
    v_child_group  BIGINT;
    v_parent_group BIGINT;
    v_parent_type  VARCHAR;
BEGIN
    SELECT group_id INTO v_child_group FROM tourist WHERE id = NEW.id;

    SELECT group_id, tourist_type INTO v_parent_group, v_parent_type
    FROM tourist WHERE id = NEW.parent_id;

    IF v_parent_type = 'CHILD' THEN
        RAISE EXCEPTION 'Parent of CHILD must not be CHILD himself (child_id=%, parent_id=%)',
            NEW.id, NEW.parent_id
            USING ERRCODE = 'check_violation';
    END IF;

    IF v_child_group IS DISTINCT FROM v_parent_group THEN
        RAISE EXCEPTION 'CHILD and parent must belong to the same tourist_group (child_group=%, parent_group=%)',
            v_child_group, v_parent_group
            USING ERRCODE = 'check_violation';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;;

DROP TRIGGER IF EXISTS trg_child_parent_consistency ON child;;

CREATE TRIGGER trg_child_parent_consistency
    BEFORE INSERT OR UPDATE ON child
    FOR EACH ROW EXECUTE FUNCTION trg_child_parent_consistency_fn();;

-- 5) Tourist.tourist_type должен соответствовать наличию записи в подтаблице (rest_tourist / shop_tourist / child).

CREATE OR REPLACE FUNCTION trg_subtype_matches_discriminator_fn()
    RETURNS TRIGGER AS $$
DECLARE
    v_actual_type VARCHAR;
    v_expected    VARCHAR := TG_ARGV[0];
BEGIN
    SELECT tourist_type INTO v_actual_type FROM tourist WHERE id = NEW.id;
    IF v_actual_type IS DISTINCT FROM v_expected THEN
        RAISE EXCEPTION 'Subtype mismatch: tourist.tourist_type=% but row inserted into %_tourist (id=%)',
            v_actual_type, lower(v_expected), NEW.id
            USING ERRCODE = 'check_violation';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;;

DROP TRIGGER IF EXISTS trg_rest_tourist_discriminator ON rest_tourist;;

CREATE TRIGGER trg_rest_tourist_discriminator
    BEFORE INSERT OR UPDATE ON rest_tourist
    FOR EACH ROW EXECUTE FUNCTION trg_subtype_matches_discriminator_fn('REST');;

DROP TRIGGER IF EXISTS trg_shop_tourist_discriminator ON shop_tourist;;

CREATE TRIGGER trg_shop_tourist_discriminator
    BEFORE INSERT OR UPDATE ON shop_tourist
    FOR EACH ROW EXECUTE FUNCTION trg_subtype_matches_discriminator_fn('SHOP');;

DROP TRIGGER IF EXISTS trg_child_discriminator ON child;;

CREATE TRIGGER trg_child_discriminator
    BEFORE INSERT OR UPDATE ON child
    FOR EACH ROW EXECUTE FUNCTION trg_subtype_matches_discriminator_fn('CHILD');;
