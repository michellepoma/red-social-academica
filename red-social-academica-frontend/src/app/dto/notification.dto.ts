

export interface NotificationDTO {
  id: number;
message: string;
read: boolean;
createdAt: string;      // o Date, si lo convertís luego
targetUrl?: string;
recipientId?: number;
recipientName?: string;
fechaAlta?: string;
fechaModificacion?: string;
motivoBaja?: string;
fechaBaja?: string;
activo?: boolean;
}
