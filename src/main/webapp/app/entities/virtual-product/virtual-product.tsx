import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './virtual-product.reducer';
import { IVirtualProduct } from 'app/shared/model/virtual-product.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProduct = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const virtualProductList = useAppSelector(state => state.virtualProduct.entities);
  const loading = useAppSelector(state => state.virtualProduct.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="virtual-product-heading" data-cy="VirtualProductHeading">
        Virtual Products
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Virtual Product
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {virtualProductList && virtualProductList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Allow Affinity</th>
                <th>Allow Campaign</th>
                <th>Code</th>
                <th>Effective Date</th>
                <th>Expiration Date</th>
                <th>Locked</th>
                <th>Name</th>
                <th>Product Code</th>
                <th>Virtual Product Type</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {virtualProductList.map((virtualProduct, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${virtualProduct.id}`} color="link" size="sm">
                      {virtualProduct.id}
                    </Button>
                  </td>
                  <td>{virtualProduct.allowAffinity ? 'true' : 'false'}</td>
                  <td>{virtualProduct.allowCampaign ? 'true' : 'false'}</td>
                  <td>{virtualProduct.code}</td>
                  <td>
                    {virtualProduct.effectiveDate ? (
                      <TextFormat type="date" value={virtualProduct.effectiveDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {virtualProduct.expirationDate ? (
                      <TextFormat type="date" value={virtualProduct.expirationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{virtualProduct.locked ? 'true' : 'false'}</td>
                  <td>{virtualProduct.name}</td>
                  <td>{virtualProduct.productCode}</td>
                  <td>{virtualProduct.virtualProductType}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${virtualProduct.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${virtualProduct.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProduct.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Virtual Products found</div>
        )}
      </div>
    </div>
  );
};

export default VirtualProduct;
