import { useQuery } from '@tanstack/react-query'

import { fetchChallenges } from '../api/challenges'
import { ChallengeTable } from '../components/ChallengeTable'

export function ChallengesPage() {
    const challengesQuery = useQuery({
        queryKey: ['challenges'],
        queryFn: fetchChallenges,
    })

    if (challengesQuery.isPending) {
        return <main className="dashboard">Loading challenges…</main>
    }

    if (challengesQuery.isError) {
        return (
            <main className="dashboard">
                <h1>Unable to load challenges</h1>
                <p>{challengesQuery.error.message}</p>
            </main>
        )
    }

    const challenges = challengesQuery.data

    return (
        <main className="dashboard">
            <section className="challenges-section route-section">
                <div className="section-heading">
                    <div>
                        <p className="eyebrow">Learning history</p>
                        <h1>Challenges</h1>
                    </div>

                    <span>{challenges.length} records</span>
                </div>

                <ChallengeTable challenges={challenges} />
            </section>
        </main>
    )
}