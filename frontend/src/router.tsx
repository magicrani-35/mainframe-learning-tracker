import {
    createRootRoute,
    createRoute,
    createRouter,
} from '@tanstack/react-router';

import App from './App';
import { ChallengeDetailPage } from './pages/ChallengeDetailPage';
import { EvidencePage } from './pages/evidencePage.tsx';
import { ChallengesPage } from "./pages/ChallengePage.tsx";
import { DashboardPage } from "./pages/DashboardPage.tsx";
import { NewChallengePage } from "./pages/NewChallengePage.tsx";
import { EditChallengePage } from "./pages/EditChallengePage.tsx";

const rootRoute = createRootRoute({
    component: App,
    notFoundComponent: () => (
        <main className={"dashboard"}>
            <h1>Page not found</h1>
        </main>
    ),
})

const dashboardRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: '/',
    component: DashboardPage,
})

const challengesRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: '/challenges',
    component: ChallengesPage,
})

const newChallengeRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: '/challenges/new',
    component: NewChallengePage,
})

const challengeDetailRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: '/challenges/$code',
    component: ChallengeDetailPage,
})

const editChallengeRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: '/challenges/$code/edit',
    component: EditChallengePage,
})

const evidenceRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: '/evidence',
    component: EvidencePage,
})

const routeTree = rootRoute.addChildren([
    dashboardRoute,
    challengesRoute,
    newChallengeRoute,
    challengeDetailRoute,
    editChallengeRoute,
    evidenceRoute,
])

export const router = createRouter({
    routeTree,
    defaultPreload: 'intent',
})

declare module '@tanstack/react-router' {
    interface Register {
        router: typeof router
    }
}