import dayjs from 'dayjs';

export interface IPeople {
  id?: number;
  peopleId?: string;
  personalData?: string | null;
  demographicData?: string | null;
  dateOfContact?: string;
  employee?: string | null;
  reason?: string | null;
  description?: string | null;
  action?: string | null;
}

export const defaultValue: Readonly<IPeople> = {};
