package net.poundex.releaseman
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProjectController
{
    static scaffold = true
}
