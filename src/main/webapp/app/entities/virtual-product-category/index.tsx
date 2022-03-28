import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VirtualProductCategory from './virtual-product-category';
import VirtualProductCategoryDetail from './virtual-product-category-detail';
import VirtualProductCategoryUpdate from './virtual-product-category-update';
import VirtualProductCategoryDeleteDialog from './virtual-product-category-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VirtualProductCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VirtualProductCategoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VirtualProductCategoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={VirtualProductCategory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VirtualProductCategoryDeleteDialog} />
  </>
);

export default Routes;
