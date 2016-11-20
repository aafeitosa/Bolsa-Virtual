package br.ucs.lasis.core.view.components;

import br.ucs.lasis.core.jpa.PagedResult;

public interface IGridLazyLoader<T> {
	
	PagedResult<T> load(GridLazyLoaderDTO dto);

}
