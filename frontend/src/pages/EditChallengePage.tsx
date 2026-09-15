import { useForm }  from '@tanstack/react-form'
import {
    useMutation,
    useQuery,
    useQueryClient,
} from '@tanstack/react-query'
import {
    Link,
    useNavigate,
    useParams,
} from "@tanstack/react-router";

import {
    fetchChallenge,
    updateChallenge,
    type Challenge,
    type ChallengeUpdate,
} from '../api/challenges'

export function EditChallengePage() {
    const { code } = useParams({
        from: '/challenges/$code/edit',
    })

    const challengeQuery = useQuery({
        queryKey: ['challenge', code],
        queryFn: () => fetchChallenge(code),
    })

    if (challengeQuery.isPending) {
        return (
            <main className={"dashboard"}>
                Loading challenges...
            </main>
        )
    }

    if (challengeQuery.isError) {
        return (
            <main className={"dashboard"}>
                <Link to="/challenges" className={"back-link"}>
                    ← Back to challenges
                </Link>

                <h1>Unable to load challenge</h1>
                <p>{challengeQuery.error.message}</p>
            </main>
        )
    }

    return (
        <ChallengeEditForm
            challenge={challengeQuery.data}
        />
    )
}

function ChallengeEditForm({
    challenge,
                           }:{
    challenge: Challenge
}) {
    const queryClient = useQueryClient()
    const navigate = useNavigate()

    const updateMutation = useMutation({
        mutationFn: (update: ChallengeUpdate) =>
            updateChallenge(challenge.code, update),

        onSuccess: async (updateChallenge) => {
            queryClient.setQueryData(
                ['challenge', challenge.code],
                updateChallenge,
            )

            await Promise.all([
                queryClient.invalidateQueries({
                    queryKey: ['challenges'],
                }),
                queryClient.invalidateQueries({
                    queryKey: ['progress'],
                }),
            ])

            await navigate({
                to: '/challenges/$code',
                params: {
                    code: updateChallenge.code,
                },
            })
        },
    })

    const form = useForm({
        defaultValues: {
            title: challenge.title,
            category: challenge.category,
            course: challenge.course,
            status: challenge.status,
            startedOn: challenge.startedOn,
            completedOn: challenge.completedOn,
            notes: challenge.notes,
        } satisfies ChallengeUpdate,

        onSubmit: async ({ value }) => {
            await updateMutation.mutateAsync({
                ...value,
                title: value.title.trim(),
                category: value.category.trim().toUpperCase(),
                course: value.course.trim(),
                notes: value.notes?.trim() || null,
            })
        },
    })

    return (
        <main className={"dashboard"}>
            <Link
                to="/challenges/$code"
                params={{ code: challenge.code }}
                className={"back-link"}
            >
                ← Back to challenge
            </Link>

            <section className={"challenge-form-section"}>
                <p className={"eyebrow"}>
                    {challenge.code}
                </p>
                <h1>Edit challenge</h1>

                <form
                    className={"challenge-form"}
                    onSubmit={(event) => {
                        event.preventDefault()
                        event.stopPropagation()
                        void form.handleSubmit()
                    }}
                >
                    <div className={"form-grid"}>
                        <form.Field
                            name="title"
                            validators={{
                                onChange: ({ value }) =>
                                    value.trim() ? undefined : 'Title is required',
                            }}
                        >
                            {(field) => (
                                <label className={"form-field"}>
                                    <span>Title</span>
                                    <input
                                        type={"text"}
                                        value={field.state.value}
                                        onBlur={field.handleBlur}
                                        onChange={(event) => field.handleChange(event.target.value)}
                                        />

                                    {field.state.meta.isTouched && field.state.meta.errors.length > 0 && (
                                        <small className={"field-error"}>
                                            {field.state.meta.errors.join(', ')}
                                        </small>
                                    )}
                                </label>
                            )}
                        </form.Field>

                        <form.Field
                            name={"category"}
                            validators={{
                                onChange: ({ value }) =>
                                    value.trim() ? undefined : 'Category is required',
                            }}
                        >
                            {(field) => (
                                <label className={"form-field"}>
                                    <span>Category</span>
                                    <input
                                        type={"text"}
                                        value={field.state.value}
                                        onBlur={field.handleBlur}
                                        onChange={(event) => field.handleChange(event.target.value)}
                                    />

                                    {field.state.meta.isTouched && field.state.meta.errors.length > 0 && (
                                        <small className={"field-error"}>
                                            {field.state.meta.errors.join(', ')}
                                        </small>
                                    )}
                                </label>
                            )}
                        </form.Field>

                        <form.Field
                            name={"course"}
                            validators={{
                        onChange: ({ value }) =>
                            value.trim() ? undefined : 'Course is required',
                            }}
                        >
                            {(field) => (
                                <label className={"form-field"}>
                                    <span>Course</span>
                                    <input
                                        type={"text"}
                                        value={field.state.value}
                                        onBlur={field.handleBlur}
                                        onChange={(event) => field.handleChange(event.target.value)}
                                    />

                                    {field.state.meta.isTouched && field.state.meta.errors.length > 0 && (
                                        <small className={"field-error"}>
                                            {field.state.meta.errors.join(", ")}
                                        </small>
                                    )}
                                </label>
                            )}
                        </form.Field>

                        <form.Field name={"status"}>
                            {(field) => (
                                <label className={"form-field"}>
                                    <span>Status</span>
                                    <select
                                        value={field.state.value}
                                        onChange={(event) => field.handleChange(event.target.value)}
                                    >
                                        <option value={"planned"}>Planned</option>
                                        <option value={"in progress"}>In progress</option>
                                        <option value={"completed"}>Completed</option>
                                        <option value={"blocked"}>Blocked</option>
                                    </select>
                                </label>
                            )}
                        </form.Field>

                        <form.Field name={"startedOn"}>
                            {(field) => (
                                <label className={"form-field"}>
                                    <span>Started on</span>
                                    <input
                                        type={"date"}
                                        value={field.state.value ?? ''}
                                        onChange={(event) => field.handleChange(event.target.value || null,)}
                                    />
                                </label>
                            )}
                        </form.Field>

                        <form.Field name={"completedOn"}>
                            {(field) => (
                                <label className={"form-field"}>
                                    <span>Completed on</span>
                                    <input
                                        type={"date"}
                                        value={field.state.value ?? ''}
                                        onChange={(event) => field.handleChange(event.target.value || null)}
                                    />
                                </label>
                            )}
                        </form.Field>
                    </div>

                    <form.Field name={"notes"}>
                        {(field) => (
                            <label className={"form-field notes-field"}>
                                <span>Notes</span>
                                <textarea
                                    rows={5}
                                    value={field.state.value ?? ''}
                                    placeholder={"What did you learn? What do you need to work on?"}
                                    onChange={(event) => field.handleChange(event.target.value || null,)}
                                />
                            </label>
                        )}
                    </form.Field>

                    {updateMutation.isError && (
                        <p className={"form-error"} role={"alert"}>
                            {updateMutation.error.message}
                        </p>
                    )}

                    <div className={"form-actions"}>
                        <form.Subscribe
                            selector={(state) => [
                                state.canSubmit,
                                state.isSubmitting,
                            ]}
                        >
                            {([canSubmit, isSubmitting]) => (
                                <button
                                    className={"submit-button"}
                                    type={"submit"}
                                    disabled={!canSubmit || isSubmitting}
                                >
                                    {isSubmitting
                                        ? 'Saving...'
                                        : 'Save changes'}
                                </button>
                            )}
                        </form.Subscribe>

                        <Link
                            to="/challenges/$code"
                            params={{ code: challenge.code }}
                            className={"cancel-button"}
                        >
                            Cancel
                        </Link>
                    </div>
                </form>
            </section>
        </main>
    )
}