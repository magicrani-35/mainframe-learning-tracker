import { useQuery } from '@tanstack/react-query'
import { fetchEvidence } from '../api/evidence'


export function EvidencePage() {
    const evidenceQuery = useQuery({
        queryKey: ['evidence'],
        queryFn: fetchEvidence,
    })

    if (evidenceQuery.isPending) {
        return <main className="dashboard">Loading evidence...</main>
    }

    if (evidenceQuery.isError) {
        return (
            <main className="dashboard">
                <h1>Unable to load evidence</h1>
                <p role="alert">{evidenceQuery.error.message}</p>
                <button onClick={() => void evidenceQuery.refetch()}>
                    Try again
                </button>
            </main>
        )
    }

    const records = evidenceQuery.data

    return (
        <main className="dashboard">
            <section className="challenges-section route-section">
                <div className="section-heading">
                    <div>
                        <p className="eyebrow">Mainframe activity</p>
                        <h1>Evidence</h1>
                    </div>

                    <button
                        disabled={evidenceQuery.isFetching}
                        onClick={() => void evidenceQuery.refetch()}
                    >
                        {evidenceQuery.isFetching ? 'Refreshing...' : 'Refresh'}
                    </button>
                </div>

                <p>{records.length}</p>
                <p>
                    Observations support your learning history. They do not automatically confirm course completion.
                </p>

                {records.length === 0 ? (
                    <p>No evidence has been imported yet.</p>
                ) : (
                    <ul>
                        {records.map((record) => (
                            <li key={record.id}>
                                <article>
                                    <h2>
                                        {record.name || record.externalId}
                                    </h2>

                                    <dl>
                                        <dt>External ID</dt>
                                        <dd>{record.externalId}</dd>

                                        <dt>Type</dt>
                                        <dd>
                                            {record.evidenceType}
                                        </dd>

                                        <dt>Source</dt>
                                        <dd>
                                            {record.sourceSystem}
                                        </dd>

                                        <dt>Challenge</dt>
                                        <dd>
                                            {record.challengeCode || 'Unlinked'}
                                        </dd>

                                        <dt>Status</dt>
                                        <dd>
                                            {record.status || 'Unknown'}
                                        </dd>

                                        <dt>Return Code</dt>
                                        <dd>
                                            {record.returnCode || 'Not available'}
                                        </dd>

                                        <dt>Furst observed</dt>
                                        <dd>
                                            {new Date(record.firstObservedAt,).toLocaleString()}
                                        </dd>

                                        <dt>Last observed</dt>
                                        <dd>
                                            {new Date(record.lastObservedAt,).toLocaleString()}
                                        </dd>
                                    </dl>
                                </article>
                            </li>
                        ))}
                    </ul>
                )}
            </section>
        </main>
    )
}