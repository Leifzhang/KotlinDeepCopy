package com.bennyhuo.kotlin.deepcopy.compiler.apt

import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import kotlinx.metadata.*
import kotlinx.metadata.jvm.KotlinClassMetadata

class KClassMirror(kotlinClassMetadata: KotlinClassMetadata.Class) {

    data class Component(val name: String, val type: TypeName) {

        val typeElement: KTypeElement? by lazy {
            KTypeElement.from(type)
        }
        
        val typeArgumentElements: List<KTypeElement?> by lazy {
            if (type is ParameterizedTypeName) {
                type.typeArguments.map {
                    KTypeElement.from(it)
                }
            } else {
                emptyList()
            }
        }
    }

    var isData: Boolean = false
        private set

    val components = mutableListOf<Component>()

    val typeParameters = mutableListOf<KmTypeParameterVisitorImpl>()

    init {
        kotlinClassMetadata.accept(object : KmClassVisitor() {
            override fun visit(flags: Flags, name: ClassName) {
                super.visit(flags, name)
                isData = Flag.Class.IS_DATA(flags)
            }

            override fun visitTypeParameter(
                flags: Flags,
                name: String,
                id: Int,
                variance: KmVariance
            ): KmTypeParameterVisitor {
                return KmTypeParameterVisitorImpl(flags, name, id, variance).also { typeParameters += it }
            }

            override fun visitConstructor(flags: Flags): KmConstructorVisitor? {
                if (!Flag.Constructor.IS_SECONDARY(flags)) {
                    return object : KmConstructorVisitor() {
                        override fun visitValueParameter(
                            flags: Flags,
                            parameterName: String
                        ): KmValueParameterVisitor {
                            return object : KmValueParameterVisitor() {
                                override fun visitType(flags: Flags): KmTypeVisitor {
                                    return object: KmTypeVisitorImpl(flags, typeParameters){
                                        override fun visitEnd() {
                                            super.visitEnd()
                                            components += Component(parameterName, type)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return super.visitConstructor(flags)
            }
        })
    }

    override fun toString(): String {
        return "isData=$isData, components=${components.joinToString()}"
    }
}