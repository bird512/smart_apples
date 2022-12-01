import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LoanSubscription from './loan-subscription';
import LoanSubscriptionDetail from './loan-subscription-detail';
import LoanSubscriptionUpdate from './loan-subscription-update';
import LoanSubscriptionDeleteDialog from './loan-subscription-delete-dialog';

const LoanSubscriptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LoanSubscription />} />
    <Route path="new" element={<LoanSubscriptionUpdate />} />
    <Route path="/:loanId/new" element={<LoanSubscriptionUpdate />} />
    <Route path=":id">
      <Route index element={<LoanSubscriptionDetail />} />
      <Route path="edit" element={<LoanSubscriptionUpdate />} />
      <Route path="delete" element={<LoanSubscriptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LoanSubscriptionRoutes;
