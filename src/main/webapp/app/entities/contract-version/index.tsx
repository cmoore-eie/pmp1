import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ContractVersion from './contract-version';
import ContractVersionDetail from './contract-version-detail';
import ContractVersionUpdate from './contract-version-update';
import ContractVersionDeleteDialog from './contract-version-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ContractVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ContractVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ContractVersionDetail} />
      <ErrorBoundaryRoute path={match.url} component={ContractVersion} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ContractVersionDeleteDialog} />
  </>
);

export default Routes;
