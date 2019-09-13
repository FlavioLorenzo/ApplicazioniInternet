import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'roleLabel'})
export class LabelRolePipe implements PipeTransform {
  transform(role): string {
    switch (role.name) {
        case 'ROLE_SYS_ADMIN': return 'System Admin';
        case 'ROLE_USER': return 'Parent';
        case 'ROLE_ESCORT': return 'Escort';
        case 'ROLE_ADMIN': return 'Admin';
        default: return 'UNKNOWN';
    }
  }
}
