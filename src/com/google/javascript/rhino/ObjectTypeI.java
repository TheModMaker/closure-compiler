/*
 *
 * ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Rhino code, released
 * May 6, 1999.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by the Initial Developer are Copyright (C) 1997-1999
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Ben Lickly
 *   Dimitris Vardoulakis
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU General Public License Version 2 or later (the "GPL"), in which
 * case the provisions of the GPL are applicable instead of those above. If
 * you wish to allow use of your version of this file only under the terms of
 * the GPL and not to allow others to use your version of this file under the
 * MPL, indicate your decision by deleting the provisions above and replacing
 * them with the notice and other provisions required by the GPL. If you do
 * not delete the provisions above, a recipient may use your version of this
 * file under either the MPL or the GPL.
 *
 * ***** END LICENSE BLOCK ***** */

package com.google.javascript.rhino;

import com.google.common.collect.ImmutableList;

/**
 * @author blickly@google.com (Ben Lickly)
 * @author dimvar@google.com (Dimitris Vardoulakis)
 */
public interface ObjectTypeI extends TypeI {

  /**
   * Gets this object's constructor, or null if it is a native
   * object (constructed natively vs. by instantiation of a function).
   */
  FunctionTypeI getConstructor();

  FunctionTypeI getSuperClassConstructor();

  /**
   * Returns the __proto__ object of this object type, NOT the ".prototype" property.
   */
  ObjectTypeI getPrototypeObject();

  // TODO(aravindpg): might be better to define a PropertyI interface and
  // then have a more general-purpose getProperty method here.

  JSDocInfo getOwnPropertyJSDocInfo(String propertyName);

  JSDocInfo getPropertyJSDocInfo(String propertyName);

  Node getOwnPropertyDefSite(String propertyName);

  Node getPropertyDefSite(String propertyName);

  /** Whether this type is an instance object of some constructor. */
  // NOTE(dimvar): for OTI, this is true only for InstanceObjectType and a single case
  // in FunctionType.java. Why do we need the FunctionType case? Could this method be
  // true for "classy" objects only?
  boolean isInstanceType();

  ObjectTypeI getRawType();

  /**
   * For an instantiated generic type, return the types that the type variables are mapped to.
   * @return Returns null if this type is not generic.
   *
   * TODO(dimvar): this could have a better name, but it's used by non-compiler code.
   * Rename in a follow-up CL.
   *
   * TODO(dimvar): After deleting the old type checker, change the signature of this and other
   * methods in TypeI to stop using wildcards.
   */
  ImmutableList<? extends TypeI> getTemplateTypes();

  /**
   * When this type represents an instance of a generic class/interface Foo, return an instance
   * of Foo with type parameters mapped to the unknown type.
   */
  ObjectTypeI instantiateGenericsWithUnknown();

  boolean hasProperty(String propertyName);

  boolean hasOwnProperty(String propertyName);

  Iterable<String> getOwnPropertyNames();

  /**
   * Works around the OTI distinction between prototype-object types and other objects.
   *
   * This method is a hack and can be deleted, either
   * - when NTI treats objects in .prototype properties specially, or
   * - when OTI is deleted.
   * Currently, if Foo extends Bar, for the Bar instance pointed to by Foo.prototype,
   * this method returns a Foo in OTI and a Bar in NTI.
   */
  ObjectTypeI normalizeObjectForCheckAccessControls();

  /**
   * Returns true if this object is an anonymous object type (i.e. the builtin Object type, or an
   * object literal). Everything else has a named reference type and returns false.
   */
  boolean isUnknownObject();

  /**
   * The old type checker uses NamedType to wrap types (e.g., defined by typedefs), and to represent
   * unresolved forward declares. NTI does not do that; unresolved types just become unknown.
   * This method always returns false for NTI types.
   */
  boolean isLegacyNamedType();

  /**
   * Only called on instances of NamedType. See also isLegacyNamedType.
   */
  TypeI getLegacyResolvedType();

  /**
   * Given an interface and a property, finds a top-most super interface
   * that has the property defined (including this interface).
   * If more than one interfaces have the property, the result is order-specific.
   * Returns a type representing an instance of the interface, not the constructor.
   */
  ObjectTypeI getTopDefiningInterface(String propName);

  /**
   * If this type represents the object in a function's prototype property, return that function.
   */
  FunctionTypeI getOwnerFunction();

  /**
   * Returns the type of property propName on this object, or null if the property doesn't exist.
   */
  TypeI getPropertyType(String propName);

  /**
   * If this type is an enum object, returns the declared type of the elements.
   * Otherwise returns null.
   */
  TypeI getEnumeratedTypeOfEnumObject();
}
