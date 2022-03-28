import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Agreement from './agreement';
import Contract from './contract';
import ContractVersion from './contract-version';
import Scheme from './scheme';
import VirtualProduct from './virtual-product';
import VirtualProductCategory from './virtual-product-category';
import VirtualProductContract from './virtual-product-contract';
import VirtualProductFlavour from './virtual-product-flavour';
import VirtualProductLine from './virtual-product-line';
import VirtualProductOrganization from './virtual-product-organization';
import VirtualProductSeasoning from './virtual-product-seasoning';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}agreement`} component={Agreement} />
      <ErrorBoundaryRoute path={`${match.url}contract`} component={Contract} />
      <ErrorBoundaryRoute path={`${match.url}contract-version`} component={ContractVersion} />
      <ErrorBoundaryRoute path={`${match.url}scheme`} component={Scheme} />
      <ErrorBoundaryRoute path={`${match.url}virtual-product`} component={VirtualProduct} />
      <ErrorBoundaryRoute path={`${match.url}virtual-product-category`} component={VirtualProductCategory} />
      <ErrorBoundaryRoute path={`${match.url}virtual-product-contract`} component={VirtualProductContract} />
      <ErrorBoundaryRoute path={`${match.url}virtual-product-flavour`} component={VirtualProductFlavour} />
      <ErrorBoundaryRoute path={`${match.url}virtual-product-line`} component={VirtualProductLine} />
      <ErrorBoundaryRoute path={`${match.url}virtual-product-organization`} component={VirtualProductOrganization} />
      <ErrorBoundaryRoute path={`${match.url}virtual-product-seasoning`} component={VirtualProductSeasoning} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
