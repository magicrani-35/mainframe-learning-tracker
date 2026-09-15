import { useForm } from '@tanstack/react-form'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from "@tanstack/react-router";

import {
    createChallenge,
    type NewChallenge,
} from '../api/challenges'

const defaultValues: NewChallenge = {
    code: '',
    title: '',
    category: '',
    course: '',
    status: 'planned',
    startedOn: null,
    completedOn: null,
    notes: null,
}

export function NewChallengePage() {
    const queryClient = useQueryClient()
    const navigate = useNavigate()

    const createMutation = useMutation({
        mutationFn: createChallenge,
        onSuccess: async (challenge) => {
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
                params: { code: challenge.code },
            })
        },
    })

    const form = useForm({
        defaultValues,
        onSubmit: async ({ value }) => {
            await createMutation.mutateAsync({
                ...value,
                code: value.code.trim().toUpperCase(),
                title: value.title.trim(),
                category: value.category.trim().toUpperCase(),
                course: value.course.trim(),
                notes: value.notes?.trim() || null,
            })
        },
    })

    return (
        <main className={"dashboard"}>
            <section className={"challenge-form-section"}>
                <p className={"eyebrow"}>Learning history</p>

                <h1>New Challenge</h1>
                <p className={"intro"}>
                    Add a course challenge and begin tracking its progress.
                </p>

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
                            name="code"
                            validators={{
                                onChange: ({ value }) =>
                                    value.trim() ? undefined : 'Challenge code is required',
                            }}
                        >
                            {(field) => (
                                <label className={"form-field"}>
                                    <span>Challenge code</span>

                                    <input
                                        type={"text"}
                                        placeholder={"Example: JAVA1"}
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
                                        placeholder={"Example Java on z/OS"}
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
                    </div>

                    <div
                        className={"form-grid form-grid-secondary"}>
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
                                        placeholder={"Example JAVA"}
                                        value={field.state.value}
                                        onBlur={field.handleBlur}
                                        onChange={(event) => field.handleChange(event.target.value)}
                                        />
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
                                        placeholder={"Example: IBM Z Xplore"}
                                        value={field.state.value}
                                        onBlur={field.handleBlur}
                                        onChange={(event) => field.handleChange(event.target.value)}
                                    />
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
                                        value={field.state.value ?? ""}
                                        onChange={(event) => field.handleChange(event.target.value || null)}
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
                                        value={field.state.value ?? ""}
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
                                    placeholder={"What did you learn? What was Difficult? What Do you need to work on?"}
                                    value={field.state.value ?? ''}
                                    onChange={(event) => field.handleChange(event.target.value || null)}
                                    />
                            </label>
                        )}
                    </form.Field>

                    {createMutation.isError && (
                        <p className={"form-error"} role="alert">
                            {createMutation.error.message}
                        </p>
                    )}

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
                                {isSubmitting ? 'Saving...' : 'Create challenge'}
                            </button>
                        )}
                    </form.Subscribe>
                </form>
            </section>
        </main>
    )
}