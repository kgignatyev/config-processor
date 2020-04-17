package com.kgi.config_processor

import com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.context.Context
import java.io.StringWriter
import java.util.*


object VelocityProcessor {


    fun processTemplate(template: String, config: ObjectNode): String {
        val out = StringWriter()
        val velocityContext: Context = VelocityContext()
        velocityContext.put( "cfg", ConfigResolver(config))
        val vp: Properties = Properties()
        vp.load( this.javaClass.classLoader.getResourceAsStream("velocity.properties"))
        Velocity.init(vp)
        Velocity.evaluate(velocityContext, out, template.hashCode().toString(), template)
        return out.toString()
    }

}


class ConfigResolver(val config: ObjectNode)  {


     fun get(exp: String): Any {
        return  ExpressionResolver.nodeValueOf( config, exp)
    }



}
