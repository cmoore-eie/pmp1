import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Agreement from './agreement';
import AgreementDetail from './agreement-detail';
import AgreementUpdate from './agreement-update';
import AgreementDeleteDialog from './agreement-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AgreementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AgreementUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AgreementDetail} />
      <ErrorBoundaryRoute path={match.url} component={Agreement} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AgreementDeleteDialog} />
  </>
);

export default Routes;
