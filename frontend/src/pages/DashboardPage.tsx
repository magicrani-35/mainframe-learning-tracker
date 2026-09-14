import { useQuery } from '@tanstack/react-query'

import { fetchProgress } from '../api/progress'

export function DashboardPage() {
    const progressQuery = useQuery({
        queryKey: ['progress'],
        queryFn: fetchProgress,
    })

    if (progressQuery.isPending) {
        return <main className={"dashboard"}>Loading progress...</main>
    }

    if (progressQuery.isError) {
        return (
            <main className={"dashboard"}>
                <h1>Unable to load progress</h1>
                <p>{progressQuery.error.message}</p>
            </main>
        )
    }

    const progress = progressQuery.data;

    return (
        <main className={"dashboard"}>
            <header>
                <p className={"eyebrow"}>
                    Barbara's learning journey
                </p>
                <h1>Mainframe Learning Tracker</h1>
                <p className={"intro"}>
                    Tracking progress across mainframe development, enterprise systems, and modern full-stack engineering.
                </p>
            </header>

            <section
                className={"summary-grid"}
                aria-label={"Progress summary"}
            >
                <article className={"summary-card"}>
                    <span>Total challenges</span>
                    <strong>{progress.total}</strong>
                </article>

                <article className={"summary-card completed"}>
                    <span>Completed</span>
                    <strong>{progress.completed}</strong>
                </article>

                <article className={"summary-card"}>
                    <span>In progress</span>
                    <strong>{progress.inProgress}</strong>
                </article>

                <article className={"summary-card"}>
                    <span>Planned</span>
                    <strong>{progress.planned}</strong>
                </article>

                <article className={"summary-card"}>
                    <span>Blocked</span>
                    <strong>{progress.blocked}</strong>
                </article>
            </section>

            <section className={"progress-panel"}>
                <div className={"progress-heading"}>
                    <h2>Overall completion</h2>
                    <strong>
                        {Math.round(progress.completionPercentage)}%
                    </strong>
                </div>

                <div
                    className={"progress-track"}
                    role={"progressbar"}
                    aria-valuemin={0}
                    aria-valuemax={100}
                    aria-valuenow={progress.completionPercentage}
                >
                    <div
                        className={"progress-value"}
                        style={{
                            width: `${progress.completionPercentage}%`,
                        }}
                    >
                    </div>
                </div>
            </section>
        </main>
    )
}