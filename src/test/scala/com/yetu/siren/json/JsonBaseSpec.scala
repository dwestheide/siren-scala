package com.yetu.siren.json

import com.yetu.siren.model.{ Entity, Property, Action, Link }
import org.scalatest.WordSpec

import scalaz.NonEmptyList
import scalaz.std.option._

trait JsonBaseSpec extends WordSpec {

  import scalaz.syntax.nel._

  protected lazy val embeddedLinkJsonString =
    """
      {
        "class": [ "items", "collection" ],
        "rel": [ "http://x.io/rels/order-items" ],
        "href": "http://api.x.io/orders/42/items"
      }
    """.stripMargin

  protected lazy val embeddedLink = Entity.EmbeddedLink(
    rel = "http://x.io/rels/order-items".wrapNel,
    href = "http://api.x.io/orders/42/items",
    classes = some(NonEmptyList("items", "collection"))
  )

  protected lazy val embeddedRepresentationJsonString =
    """
      {
        "class": [ "info", "customer" ],
        "rel": [ "http://x.io/rels/customer" ],
        "properties": {
          "customerId": "pj123",
          "name": "Peter Joseph"
        },
        "entities": [
          {
            "class": [ "company" ],
            "rel": [ "http://x.io/rels/company" ],
            "href": "http://api.x.io/customer/pj123/company"
          }
        ],
        "actions": [
          {
            "name": "set-name",
            "title": "Set Customer's Name",
            "method": "POST",
            "href": "http://api.x.io/customer/pj123/name",
            "type": "application/json",
            "fields": [
              { "name": "name", "type": "text" }
            ]
          }
        ],
        "links": [
          { "rel": [ "self" ], "href": "http://api.x.io/customers/pj123" }
        ],
        "title": "Customer information"
      }
    """.stripMargin

  protected lazy val embeddedRepresentation = Entity.EmbeddedRepresentation(
    classes = some(NonEmptyList("info", "customer")),
    rel = "http://x.io/rels/customer".wrapNel,
    properties = some(NonEmptyList(
      Property("customerId", Property.StringValue("pj123")),
      Property("name", Property.StringValue("Peter Joseph")))),
    entities = some(List(
      Entity.EmbeddedLink(
        classes = some("company".wrapNel),
        rel = "http://x.io/rels/company".wrapNel,
        href = "http://api.x.io/customer/pj123/company"
      )
    )),
    actions = some(NonEmptyList(
      Action(
        name = "set-name",
        href = "http://api.x.io/customer/pj123/name",
        title = some("Set Customer's Name"),
        method = some(Action.Method.POST),
        `type` = some(Action.Encoding.`application/json`),
        fields = some(NonEmptyList(
          Action.Field(name = "name", `type` = Action.Field.Type.`text`)
        ))
      )
    )),
    links = some(Link(href = "http://api.x.io/customers/pj123", rel = "self".wrapNel).wrapNel),
    title = some("Customer information")
  )

  protected lazy val props = NonEmptyList(
    Property("orderNumber", Property.NumberValue(42)),
    Property("itemCount", Property.NumberValue(3)),
    Property("status", Property.StringValue("pending")),
    Property("foo", Property.BooleanValue(value = false)),
    Property("bar", Property.NullValue))

  protected val properties = NonEmptyList(
    Property("orderNumber", Property.NumberValue(42)),
    Property("itemCount", Property.NumberValue(3)),
    Property("status", Property.StringValue("pending")))

  protected lazy val classesJsonString =
    """[ "info", "customer" ]""".stripMargin
  protected lazy val classes = NonEmptyList("info", "customer")

  protected lazy val links = NonEmptyList(
    Link(href = "http://api.x.io/orders/42", rel = "self".wrapNel),
    Link(href = "http://api.x.io/orders/41", rel = "previous".wrapNel),
    Link(href = "http://api.x.io/orders/43", rel = "next".wrapNel)
  )

  protected lazy val action = Action(
    name = "add-item",
    href = "http://api.x.io/orders/42/items",
    title = some("Add Item"),
    method = some(Action.Method.POST),
    `type` = some(Action.Encoding.`application/x-www-form-urlencoded`),
    fields = some(NonEmptyList(
      Action.Field(name = "orderNumber", `type` = Action.Field.Type.`hidden`, value = some("42")),
      Action.Field(name = "productCode", `type` = Action.Field.Type.`text`),
      Action.Field(name = "quantity", `type` = Action.Field.Type.`number`)))
  )

  protected lazy val propsJsonString =
    """
      {
        "orderNumber": 42,
        "itemCount": 3,
        "status": "pending",
        "foo": false,
        "bar": null
      }
    """.stripMargin

  protected lazy val linksJsonString =
    """
      [
        { "rel": [ "self" ], "href": "http://api.x.io/orders/42" },
        { "rel": [ "previous" ], "href": "http://api.x.io/orders/41" },
        { "rel": [ "next" ], "href": "http://api.x.io/orders/43" }
      ]
    """.stripMargin

  protected lazy val actionJsonString =
    """
      {
        "name": "add-item",
        "title": "Add Item",
        "method": "POST",
        "href": "http://api.x.io/orders/42/items",
        "type": "application/x-www-form-urlencoded",
        "fields": [
          { "name": "orderNumber", "type": "hidden", "value": "42" },
          { "name": "productCode", "type": "text" },
          { "name": "quantity", "type": "number" }
        ]
      }
    """.stripMargin

  protected val entity = Entity.RootEntity(
    classes = some("order".wrapNel),
    properties = some(properties),
    entities = some(List(embeddedLink, embeddedRepresentation)),
    actions = some(action.wrapNel),
    links = some(links),
    title = some("Order number 42")
  )

  protected lazy val entityJsonString =
    """
          {
            "class": [ "order" ],
            "properties": {
                "orderNumber": 42,
                "itemCount": 3,
                "status": "pending"
            },
            "entities": [
              {
                "class": [ "items", "collection" ],
                "rel": [ "http://x.io/rels/order-items" ],
                "href": "http://api.x.io/orders/42/items"
              },
              {
                "class": [ "info", "customer" ],
                "rel": [ "http://x.io/rels/customer" ],
                "properties": {
                  "customerId": "pj123",
                  "name": "Peter Joseph"
                },
                "entities": [
                  {
                    "class": [ "company" ],
                    "rel": [ "http://x.io/rels/company" ],
                    "href": "http://api.x.io/customer/pj123/company"
                  }
                ],
                "actions": [
                  {
                    "name": "set-name",
                    "title": "Set Customer's Name",
                    "method": "POST",
                    "href": "http://api.x.io/customer/pj123/name",
                    "type": "application/json",
                    "fields": [
                      { "name": "name", "type": "text" }
                    ]
                  }
                ],
                "links": [
                  { "rel": [ "self" ], "href": "http://api.x.io/customers/pj123" }
                ],
                "title": "Customer information"
              }
            ],
            "actions": [
              {
                "name": "add-item",
                "title": "Add Item",
                "method": "POST",
                "href": "http://api.x.io/orders/42/items",
                "type": "application/x-www-form-urlencoded",
                "fields": [
                  { "name": "orderNumber", "type": "hidden", "value": "42" },
                  { "name": "productCode", "type": "text" },
                  { "name": "quantity", "type": "number" }
                ]
              }
            ],
            "links": [
              { "rel": [ "self" ], "href": "http://api.x.io/orders/42" },
              { "rel": [ "previous" ], "href": "http://api.x.io/orders/41" },
              { "rel": [ "next" ], "href": "http://api.x.io/orders/43" }
            ],
            "title": "Order number 42"
          }
    """.stripMargin

}
