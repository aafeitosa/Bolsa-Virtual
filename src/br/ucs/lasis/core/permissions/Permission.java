package br.ucs.lasis.core.permissions;

import br.ucs.lasis.core.enums.BaseEnum;

/**
 * Interface to represent a permission.
 * 
 * @author cassio.molin
 */
public interface Permission extends BaseEnum {

	Permission getParent();
}
