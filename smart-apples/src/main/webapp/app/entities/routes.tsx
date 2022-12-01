import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Loan from './loan';
import LoanSubscription from './loan-subscription';
import Payment from './payment';
import Message from './message';
import Address from './address';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="loan/*" element={<Loan />} />
        <Route path="loan-subscription/*" element={<LoanSubscription />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="message/*" element={<Message />} />
        <Route path="address/*" element={<Address />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
