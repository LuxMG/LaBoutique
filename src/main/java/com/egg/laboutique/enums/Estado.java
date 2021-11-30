
package com.egg.laboutique.enums;

public enum Estado {
    Entregado,  // para donaciones y deseos -> ya se entrego el pedido
    Reservado, // para donaciones -> el beneficiario pidio esa donacion
    Disponible, // para donaciones -> el donante dono algo
    Solicitado, // para deseos -> el beneficiario hizo el pedido
    Tomado // para deseos -> un donante paga el pedido
}

// para donaciones el ciclo es
// disponible -> reservado -> entregado
// para deseos es
// solicitado -> tomado -> entregado
