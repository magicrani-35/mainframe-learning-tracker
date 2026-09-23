export type Evidence = {
    id: number;
    challengeCode?: string | null;
    evidenceType: string;
    sourceSystem: string;
    externalId: string;
    name?: string | null;
    status?: string | null;
    returnCode?: string | null;
    firstObservedAt: string;
    lastObservedAt: string;
}

export async function fetchEvidence(): Promise<Evidence[]> {
    const response = await fetch('/api/evidence');
    
    if (!response.ok) {
        throw new Error(
            `Unable to load evidence: ${response.status}`,
        );
    }

    return response.json();
}