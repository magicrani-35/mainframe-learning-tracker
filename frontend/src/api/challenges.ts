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

export type NewChallenge = {
    code: string;
    title: string;
    category: string;
    course: string;
    status: string;
    startedOn: string | null;
    completedOn: string | null;
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

export async function fetchChallenge(
    code: string,
): Promise<Challenge> {
    const response = await fetch(
        `/api/challenges/${encodeURIComponent(code)}`,
    )

    if (!response.ok) {
        if (response.status === 404) {
            throw new Error(`Challenge ${code} was not found`);
        }
        throw new Error(
            `Unable to load challenge: ${response.status}`,
        )
    }

    return response.json();
}

export async function createChallenge(
    challenge: NewChallenge,
): Promise<Challenge> {
    const response = await fetch('/api/challenges', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(challenge),
    })

    if (!response.ok) {
        const message = await response.text();

        throw new Error(
            message || `Unable to create challenge: ${response.status}`,
        )
    }

    return response.json();
}