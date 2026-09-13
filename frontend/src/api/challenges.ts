export type Challenge = {
    code: string;
    title: string;
    category: string;
    course: string;
    status: string;
    startedOn: string;
    completedOn: string;
    notes: string | null;
}

export async function fetchChallenges():
Promise<Challenge[]> {
    const response = await fetch(`/api/challenges`);

    if (!response.ok) {
        throw new Error(
            `Unable to load challenges: ${response.status}`,
        )
    }

    return response.json();
}