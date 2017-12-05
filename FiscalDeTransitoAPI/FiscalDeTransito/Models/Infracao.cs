using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Spatial;
using System.Threading.Tasks;

namespace FiscalDeTransitoApi.Models
{
    public class Infracao
    {
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [Key]
        public Guid Id { get; set; }

        public String Descricao { get; set; }

        public Int64 IMEI { get; set; }

        public Double Latitude { get; set; }
        public Double Longitude { get; set; }

        public virtual InfracaoFile File { get; set; }

        public DateTime DataRegistro { get; set; }

    }
}
