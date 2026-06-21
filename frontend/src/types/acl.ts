
export interface PermissionDto {
  id: number;
  code: string;
  category: string;
  description: string;
}

export interface RoleDto {
  id?: number;
  name: string;
  description?: string;
  systemic?: boolean;
  permissionIds?: number[];
  permissionCodes?: string[];
}
