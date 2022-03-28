import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './virtual-product-category.reducer';
import { IVirtualProductCategory } from 'app/shared/model/virtual-product-category.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VirtualProductCategory = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const virtualProductCategoryList = useAppSelector(state => state.virtualProductCategory.entities);
  const loading = useAppSelector(state => state.virtualProductCategory.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="virtual-product-category-heading" data-cy="VirtualProductCategoryHeading">
        Virtual Product Categories
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Virtual Product Category
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {virtualProductCategoryList && virtualProductCategoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Ancestor Item Identifier</th>
                <th>Code</th>
                <th>Item Identifier</th>
                <th>Name</th>
                <th>Priority</th>
                <th>Virtual Product</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {virtualProductCategoryList.map((virtualProductCategory, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${virtualProductCategory.id}`} color="link" size="sm">
                      {virtualProductCategory.id}
                    </Button>
                  </td>
                  <td>{virtualProductCategory.ancestorItemIdentifier}</td>
                  <td>{virtualProductCategory.code}</td>
                  <td>{virtualProductCategory.itemIdentifier}</td>
                  <td>{virtualProductCategory.name}</td>
                  <td>{virtualProductCategory.priority}</td>
                  <td>
                    {virtualProductCategory.virtualProduct ? (
                      <Link to={`virtual-product/${virtualProductCategory.virtualProduct.id}`}>
                        {virtualProductCategory.virtualProduct.name}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductCategory.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductCategory.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${virtualProductCategory.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Virtual Product Categories found</div>
        )}
      </div>
    </div>
  );
};

export default VirtualProductCategory;
