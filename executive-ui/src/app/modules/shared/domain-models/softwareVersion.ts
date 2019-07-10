import { PatchDetail } from "./patchDetail";

export class SoftwareVersion{
    totalCount:number;
    standardVersion:string;
    updatedCount:number;
    patch: PatchDetail[];
}