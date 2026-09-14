import {
    createRootRoute,
    createRoute,
    createRouter,
} from '@tanstack/react-router';

import App from './App';
import { ChallengesPage } from "./pages/ChallengePage.tsx";
import { DashboardPage } from "./pages/DashboardPage.tsx";

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

const routeTree = rootRoute.addChildren([
    dashboardRoute,
    challengesRoute,
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