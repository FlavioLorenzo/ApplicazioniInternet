import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'roleLabel'})
export class LabelRolePipe implements PipeTransform {
  transform(role): string {
    switch (role.name) {
        case 'ROLE_USER': return 'Genitore';
        default: return 'SCONOSCIUTO';
    }
  }
}