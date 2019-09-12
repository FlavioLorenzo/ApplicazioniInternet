export class Role {
  constructor(public id_role: string | number, public name: string){}
}

export class CurrentUser {
  constructor(public id: number, public mail: string, public token: string, public role: Role) {}
}
