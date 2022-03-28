import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Scheme from './scheme';
import SchemeDetail from './scheme-detail';
import SchemeUpdate from './scheme-update';
import SchemeDeleteDialog from './scheme-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SchemeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SchemeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SchemeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Scheme} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SchemeDeleteDialog} />
  </>
);

export default Routes;
