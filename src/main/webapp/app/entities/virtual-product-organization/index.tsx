import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VirtualProductOrganization from './virtual-product-organization';
import VirtualProductOrganizationDetail from './virtual-product-organization-detail';
import VirtualProductOrganizationUpdate from './virtual-product-organization-update';
import VirtualProductOrganizationDeleteDialog from './virtual-product-organization-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VirtualProductOrganizationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VirtualProductOrganizationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VirtualProductOrganizationDetail} />
      <ErrorBoundaryRoute path={match.url} component={VirtualProductOrganization} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VirtualProductOrganizationDeleteDialog} />
  </>
);

export default Routes;
