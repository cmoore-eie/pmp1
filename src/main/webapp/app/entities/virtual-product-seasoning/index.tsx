import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VirtualProductSeasoning from './virtual-product-seasoning';
import VirtualProductSeasoningDetail from './virtual-product-seasoning-detail';
import VirtualProductSeasoningUpdate from './virtual-product-seasoning-update';
import VirtualProductSeasoningDeleteDialog from './virtual-product-seasoning-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VirtualProductSeasoningUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VirtualProductSeasoningUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VirtualProductSeasoningDetail} />
      <ErrorBoundaryRoute path={match.url} component={VirtualProductSeasoning} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VirtualProductSeasoningDeleteDialog} />
  </>
);

export default Routes;
