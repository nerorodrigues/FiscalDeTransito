using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace FiscalDeTransitoApi.Models
{
    public class InfracaoFile
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }

        [ForeignKey("Infracao")]
        public Guid InfracaoId { get; set; }

        public virtual Infracao Infracao { get; set; }

        public byte[] File { get; set; }
    }
}
