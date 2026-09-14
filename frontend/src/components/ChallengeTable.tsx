import { useMemo, useState } from 'react';
import {
    createSortedRowModel,
    rowSortingFeature,
    sortFns,
    tableFeatures,
    useTable,
} from '@tanstack/react-table';
import type { ColumnDef } from '@tanstack/react-table';

import type { Challenge } from '../api/challenges'

type ChallengeTableProps = {
    challenges: Challenge[];
}

const features = tableFeatures({
    rowSortingFeature,
    sortedRowModel: createSortedRowModel(),
    sortFns,
})

function formatDate(date: string | null) {
    if (!date) {
        return 'Not recorded';
    }

    return new Intl.DateTimeFormat('en-US', {
        dateStyle: 'medium',
    }).format(new Date(`${date}T00:00:00`))
}

const columns: Array<ColumnDef<typeof features, Challenge>> = [
    {
        accessorKey: 'code',
        header: 'Code',
    },
    {
        accessorKey: 'title',
        header: 'Challenge',
    },
    {
        accessorKey: 'category',
        header: 'Category',
    },
    {
        accessorKey: 'course',
        header: 'Course',
    },
    {
        accessorKey: 'status',
        header: 'Status',
        cell: (info) => {
            const status = info.getValue<string>();

            return (
                <span
                    className={`status status-${status
                        .toLowerCase()
                        .replaceAll(' ', '_')}`}
                >
                    {status}
                </span>
            )
        },
    },
    {
        accessorKey: 'completedOn',
        header: 'Completed',
        cell: (info) =>
            formatDate(info.getValue<string | null>()),
    },
]

export function ChallengeTable({
    challenges,
                               }: ChallengeTableProps) {
    const [search, setSearch] = useState('');
    const [status, setStatus] = useState('all')

    const filteredChallenges = useMemo(() => {
        const searchValue = search.trim().toLowerCase();

        return challenges.filter((challenge) => {
            const matchesSearch =
                searchValue.length === 0 ||

                challenge.code.toLowerCase().includes(searchValue) ||
                challenge.title.toLowerCase().includes(searchValue) ||
                challenge.category.toLowerCase().includes(searchValue) ||
                challenge.course.toLowerCase().includes(searchValue)

            const matchesStatus =
                status === 'all' ||
                challenge.status.toLowerCase() === status
            return matchesSearch && matchesStatus
        })
    }, [challenges, search, status])

    const table = useTable({
        key: 'challenge-table',
        features,
        columns,
        data: filteredChallenges,
    })

    return (
        <div>
            <div className={"table-controls"}>
                <label>
                    <span>Search challenges</span>
                    <input
                        type={"search"}
                        value={search}
                        placeholder={"Search by title, code, or category"}
                        onChange={(event) => setSearch(event.target.value)
                    }
                    />
                </label>

                <label>
                    <span>Filter by status</span>
                    <select
                        value={status}
                        onChange={(event) => setStatus(event.target.value)}
                    >
                        <option value={"all"}>All statuses</option>
                        <option value={"completed"}>Completed</option>
                        <option value={"in progress"}>In progress</option>
                        <option value={"planned"}>Planned</option>
                        <option value={"blocked"}>Blocked</option>
                    </select>
                </label>
            </div>

            <div className={"table-wrapper"}>
                <table className={"challenge-table"}>
                    <thead>
                    {table.getHeaderGroups().map((headerGroup) => (
                        <tr key={headerGroup.id}>
                            {headerGroup.headers.map((header) => {
                                const sorting =
                                    header.column.getIsSorted()

                                return (
                                    <th key={header.id}>
                                        {header.isPlaceholder ? null : (
                                            <button
                                                type={"button"}
                                                className={"sort-button"}
                                                onClick={header.column.getToggleSortingHandler()}
                                            >
                                                <table.FlexRender
                                                    header={header}
                                                />

                                                {sorting === 'asc' && ' ↑'}
                                                {sorting === 'desc' && ' ↓'}
                                            </button>
                                        )}
                                    </th>
                                )
                            })}
                        </tr>
                    ))}
                    </thead>

                    <tbody>
                    {table.getRowModel().rows.map((row) => (
                        <tr key={row.id}>
                            {row.getAllCells().map((cell) => (
                                <td key={cell.id}>
                                    <table.FlexRender cell={cell} />
                                </td>
                            ))}
                        </tr>
                    ))}
                    </tbody>
                </table>

                {filteredChallenges.length === 0 && (
                    <p className={"empty-table"}>
                        No Challenges match those filters.
                    </p>
                )}
            </div>
        </div>
    )
}