import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VirtualProductContract from './virtual-product-contract';
import VirtualProductContractDetail from './virtual-product-contract-detail';
import VirtualProductContractUpdate from './virtual-product-contract-update';
import VirtualProductContractDeleteDialog from './virtual-product-contract-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VirtualProductContractUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VirtualProductContractUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VirtualProductContractDetail} />
      <ErrorBoundaryRoute path={match.url} component={VirtualProductContract} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VirtualProductContractDeleteDialog} />
  </>
);

export default Routes;
