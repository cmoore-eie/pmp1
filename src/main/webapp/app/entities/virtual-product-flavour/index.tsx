import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VirtualProductFlavour from './virtual-product-flavour';
import VirtualProductFlavourDetail from './virtual-product-flavour-detail';
import VirtualProductFlavourUpdate from './virtual-product-flavour-update';
import VirtualProductFlavourDeleteDialog from './virtual-product-flavour-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VirtualProductFlavourUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VirtualProductFlavourUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VirtualProductFlavourDetail} />
      <ErrorBoundaryRoute path={match.url} component={VirtualProductFlavour} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VirtualProductFlavourDeleteDialog} />
  </>
);

export default Routes;
