import type { ResponseRekening } from "./ResponseRekening";

export interface ResponseAkun {
    uuid: string;
    firstName: string;
    middleName: string | undefined;
    lastName: string | undefined;
    email: string;
    password: string;
    createdAt: string;
    role: string;
    rekening: ResponseRekening
}