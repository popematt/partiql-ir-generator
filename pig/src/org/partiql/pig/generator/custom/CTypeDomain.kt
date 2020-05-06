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

package org.partiql.pig.generator.custom

import org.partiql.pig.domain.model.*

//
// Types in this file are exposed to FreeMarker templates at run-time.
//

data class CTypeDomain(
    val name: String,
    val tuples: List<CTuple>,
    val sums: List<CSum>
)

data class CElement(
    val name: String,
    val type: String,
    val isVariadic: Boolean,
    val isOptional: Boolean
)

data class CTuple(
    val name: String,
    val memberOfType: String?,
    val elements: List<CElement>,
    val arity: IntRange,
    val tupleType: TupleType
) {
    /** All of the elements excluding the variadic element. */
    @Suppress("unused")
    val monadicElements = elements.filter { !it.isVariadic }

    /** There may be only one variadic element and if it's present, it's here. */
    @Suppress("MemberVisibilityCanBePrivate")
    val variadicElement = elements.singleOrNull { it.isVariadic }

    /** True when there's a variadic element. */
    @Suppress("unused")
    val hasVariadicElement = variadicElement != null
}

data class CSum(
    val name: String,
    val variants: List<CTuple>
)

fun TypeDomain.toCTypeDomain(): CTypeDomain {
    val gTuples = mutableListOf<CTuple>()
    val gSums = mutableListOf<CSum>()

    this.userTypes.forEach {
        when(it) {
            DataType.Ion, DataType.Int, DataType.Symbol ->  { /* intentionally blank */ }
            is DataType.Tuple -> gTuples.add(it.toCTuple(memberOfType = null))
            is DataType.Sum -> gSums.add(it.toCTuple())
        }
    }

    return CTypeDomain(
        name = this.name,
        tuples = gTuples,
        sums = gSums
    )
}

private fun DataType.Tuple.toCTuple(memberOfType: String?) =
    CTuple(
        name = this.tag,
        memberOfType = memberOfType,
        elements = this.namedElements.map { it.toCElement() },
        arity = this.computeArity(),
        tupleType = this.tupleType
    )

private fun NamedElement.toCElement(): CElement {
    val (isOptional, isVariadic) = when(this.typeReference.arity) {
        Arity.Required -> false to false
        Arity.Optional -> true to false
        is Arity.Variadic -> false to true
    }

    return CElement(
        name = this.name,
        type = this.typeReference.typeName,
        isOptional = isOptional,
        isVariadic = isVariadic)
}


private fun DataType.Sum.toCTuple() =
    CSum(
        name = this.tag,
        variants = this.variants.map { it.toCTuple(this.tag) }
    )