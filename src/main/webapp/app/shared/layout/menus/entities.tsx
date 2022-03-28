import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/agreement">
      Agreement
    </MenuItem>
    <MenuItem icon="asterisk" to="/contract">
      Contract
    </MenuItem>
    <MenuItem icon="asterisk" to="/contract-version">
      Contract Version
    </MenuItem>
    <MenuItem icon="asterisk" to="/scheme">
      Scheme
    </MenuItem>
    <MenuItem icon="asterisk" to="/virtual-product">
      Virtual Product
    </MenuItem>
    <MenuItem icon="asterisk" to="/virtual-product-category">
      Virtual Product Category
    </MenuItem>
    <MenuItem icon="asterisk" to="/virtual-product-contract">
      Virtual Product Contract
    </MenuItem>
    <MenuItem icon="asterisk" to="/virtual-product-flavour">
      Virtual Product Flavour
    </MenuItem>
    <MenuItem icon="asterisk" to="/virtual-product-line">
      Virtual Product Line
    </MenuItem>
    <MenuItem icon="asterisk" to="/virtual-product-organization">
      Virtual Product Organization
    </MenuItem>
    <MenuItem icon="asterisk" to="/virtual-product-seasoning">
      Virtual Product Seasoning
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
