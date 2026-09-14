 import { useQuery } from '@tanstack/react-query'
 import {
    Link,
     useParams,
 } from '@tanstack/react-router'

 import { fetchChallenge } from "../api/challenges.ts";

function formatDate(date: string | null) {
    if (!date) {
        return 'Not recorded'
    }

    return new Intl.DateTimeFormat('en-US', {
        dateStyle: 'long',
    }).format(new Date(`${date}T00:00:00`))
}

export function ChallengeDetailPage() {
    const { code } = useParams({
        from: '/challenges/$code',
    })

    const challengeQuery = useQuery({
        queryKey: ['challenge', code],
        queryFn: () => fetchChallenge(code),
    })

    if (challengeQuery.isPending) {
        return <main className={"dashboard"}>
            Loading challenge...
        </main>
    }

    if (challengeQuery.isError) {
        return (
            <main className={"dashboard"}>
                <Link to="/challenges" className={"back-link"}>
                    ← Back to challenges
                </Link>

                <h1>Unable to load challenges</h1>
                <p>{challengeQuery.error.message}</p>
            </main>
        )
    }

    const challenge = challengeQuery.data

    return (
        <main className={"dashboard"}>
            <Link to="/challenges" className={"back-link"}>
                ← Back to challenges
            </Link>

            <article className={"detail-card"}>
                <div className={"challenge-topline"}>
                    <span className={"challenge-code"}>
                        {challenge.code}
                    </span>

                    <span
                        className={`status status-${challenge.status
                            .toLowerCase()
                            .replaceAll(' ', '_')}`}
                    >
                        {challenge.status}
                    </span>
                </div>

                <h1>{challenge.title}</h1>

                <dl className={"detail-grid"}>
                    <div>
                        <dt>Category</dt>
                        <dd>{challenge.category}</dd>
                    </div>

                    <div>
                        <dt>Course</dt>
                        <dd>{challenge.course}</dd>
                    </div>

                    <div>
                        <dt>Started</dt>
                        <dd>{formatDate(challenge.startedOn)}</dd>
                    </div>

                    <div>
                        <dt>Completed</dt>
                        <dd>{formatDate(challenge.completedOn)}</dd>
                    </div>
                </dl>

                <section className={"notes-panel"}>
                    <h2>Learning notes</h2>
                    <p>
                        {challenge.notes || 'No notes recorded yet.'}
                    </p>
                </section>
            </article>
        </main>
    )
}