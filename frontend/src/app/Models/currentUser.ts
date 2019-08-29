export class CurrentUser {
  id: number;
  mail: string;
  token: string;

  constructor(id: number, mail: string, token: string) {
    this.id = id;
    this.mail = mail;
    this.token = token;
  }

}
