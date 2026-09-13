export type ProgressSummary = {
    total: number;
    completed: number;
    inProgress: number;
    planned: number;
    blocked: number;
    completionPercentage: number;
}

export async function fetchProgress():
Promise<ProgressSummary> {
    const response = await fetch('/api/progress');

    if (!response.ok) {
        throw new Error(
            `Unable to load progress: ${response.status}`,
        )
    }

    return response.json();
}