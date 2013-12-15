<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <head>
                <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

                <link href="http://getbootstrap.com/dist/css/bootstrap.min.css" rel="stylesheet"/>
                <link href="http://getbootstrap.com/dist/css/bootstrap-responsive.min.css" rel="stylesheet"/>
            </head>
            <body>
                <div class="container">
                    <h1>Welcome to our book store!</h1>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Book Id</th>
                                <th>Author</th>
                                <th>Title</th>
                                <th>Genre</th>
                                <th>Price</th>
                                <th>Publish Date</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            <xsl:for-each select="catalog/book">
                                <tr>
                                    <td>
                                        <xsl:value-of select="@id"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="author"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="title"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="genre"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="price"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="publish_date"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="description"/>
                                    </td>
                                </tr>
                            </xsl:for-each>
                        </tbody>
                    </table>
                </div>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
