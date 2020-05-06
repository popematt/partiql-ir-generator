/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 */

package org.partiql.pig.domain.model

import com.amazon.ionelement.api.MetaContainer

class TypeRef(val typeName: String, val arity: Arity, val metas: MetaContainer) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TypeRef) return false

        if (typeName != other.typeName) return false
        if (arity != other.arity) return false
        // Note [metas] intentionally not included here!

        return true
    }

    override fun hashCode(): Int {
        var result = typeName.hashCode()
        result = 31 * result + arity.hashCode()
        // Note [metas] intentionally not included here!

        return result
    }
}