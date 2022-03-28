import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VirtualProduct from './virtual-product';
import VirtualProductDetail from './virtual-product-detail';
import VirtualProductUpdate from './virtual-product-update';
import VirtualProductDeleteDialog from './virtual-product-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VirtualProductUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VirtualProductUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VirtualProductDetail} />
      <ErrorBoundaryRoute path={match.url} component={VirtualProduct} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VirtualProductDeleteDialog} />
  </>
);

export default Routes;
