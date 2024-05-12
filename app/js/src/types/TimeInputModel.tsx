export type TimeInputModel = {
    startTime: string,
    endTime: string,
    weekDay: string,
    description: string
}
export type TimeUpdateInputModel = {
    startTime: string,
    endTime: string,
    weekDay: string
};

export type TimeOutputModel = {
        id: number;
        taskId: number;
        weekDay: string;
        startTime: string;
        endTime: string;
        description: string;
}
