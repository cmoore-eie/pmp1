import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VirtualProductLine from './virtual-product-line';
import VirtualProductLineDetail from './virtual-product-line-detail';
import VirtualProductLineUpdate from './virtual-product-line-update';
import VirtualProductLineDeleteDialog from './virtual-product-line-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VirtualProductLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VirtualProductLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VirtualProductLineDetail} />
      <ErrorBoundaryRoute path={match.url} component={VirtualProductLine} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VirtualProductLineDeleteDialog} />
  </>
);

export default Routes;
