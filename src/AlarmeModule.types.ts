export interface Alarme {
    /**
     * Unique identifier for the alarm
     */
    uid?: string;

    enabled?: boolean;
    title?: string;
    description?: string;
    days?: number[];
    hour?: number;
    minutes?: number;
    snoozeInterval?: number;
    repeating?: boolean;
    active?: true;
}