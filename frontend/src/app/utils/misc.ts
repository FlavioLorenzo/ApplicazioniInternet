export class MiscUtils {
  static fromRelativeUrlToArray(url: string): Array<string> {
    const array: Array<string> = url.split('/');

    if ( array[0] === '') {
      array.shift();
    }

    array[0] = '/' + array[0];
    return array;
  }
}
