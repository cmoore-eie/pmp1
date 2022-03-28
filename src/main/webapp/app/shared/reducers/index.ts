import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
import sessions from 'app/modules/account/sessions/sessions.reducer';
// prettier-ignore
import agreement from 'app/entities/agreement/agreement.reducer';
// prettier-ignore
import contract from 'app/entities/contract/contract.reducer';
// prettier-ignore
import contractVersion from 'app/entities/contract-version/contract-version.reducer';
// prettier-ignore
import scheme from 'app/entities/scheme/scheme.reducer';
// prettier-ignore
import virtualProduct from 'app/entities/virtual-product/virtual-product.reducer';
// prettier-ignore
import virtualProductCategory from 'app/entities/virtual-product-category/virtual-product-category.reducer';
// prettier-ignore
import virtualProductContract from 'app/entities/virtual-product-contract/virtual-product-contract.reducer';
// prettier-ignore
import virtualProductFlavour from 'app/entities/virtual-product-flavour/virtual-product-flavour.reducer';
// prettier-ignore
import virtualProductLine from 'app/entities/virtual-product-line/virtual-product-line.reducer';
// prettier-ignore
import virtualProductOrganization from 'app/entities/virtual-product-organization/virtual-product-organization.reducer';
// prettier-ignore
import virtualProductSeasoning from 'app/entities/virtual-product-seasoning/virtual-product-seasoning.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  sessions,
  agreement,
  contract,
  contractVersion,
  scheme,
  virtualProduct,
  virtualProductCategory,
  virtualProductContract,
  virtualProductFlavour,
  virtualProductLine,
  virtualProductOrganization,
  virtualProductSeasoning,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
