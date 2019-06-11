export class CurrentUser {
  mail: string;
  token: string;

  constructor(mail: string, token: string) {
    this.mail = mail;
    this.token = token;
  }

}
